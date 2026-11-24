#!/usr/bin/env python3
"""Build against the extracted GTNH 2.9.0 server distribution."""

import argparse
import hashlib
import lzma
import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory
from urllib.request import urlopen
from zipfile import ZIP_DEFLATED, ZipFile

ROOT = Path(__file__).resolve().parent
MAVEN = "https://repo.maven.apache.org/maven2"
TOOLS = {
    "ecj.jar": (
        "org/eclipse/jdt/ecj/3.38.0/ecj-3.38.0.jar",
        "97c566b120009c203a2fc8b291f4a9adbc171cf1ccb70f06f6b4e1828c00ce8e",
    ),
    "specialsource.jar": (
        "net/md-5/SpecialSource/1.11.4/SpecialSource-1.11.4-shaded.jar",
        "e2cab24b1c12400ad73b15972bb21e4273a0dc7081c8b3c136ddfdd824c78518",
    ),
}


def get_tool(name, cache):
    relative_url, expected_hash = TOOLS[name]
    path = cache / name
    if path.is_file() and hashlib.sha256(path.read_bytes()).hexdigest() == expected_hash:
        return path

    with urlopen(f"{MAVEN}/{relative_url}", timeout=45) as response:
        data = response.read()
    if hashlib.sha256(data).hexdigest() != expected_hash:
        raise ValueError(f"Unexpected SHA-256 for {name}")
    path.write_bytes(data)
    return path


def build(server, output):
    forge = server / "forge-1.7.10-10.13.4.1614-1.7.10-universal.jar"
    minecraft = server / "minecraft_server.1.7.10.jar"
    dependencies = [
        server / "mods/+unimixins-all-1.7.10-0.3.1.jar",
        server / "mods/backhand-1.8.18.jar",
        server / "mods/appliedenergistics2-rv3-beta-1095-GTNH.jar",
        server / "mods/EnderIO-2.10.51.jar",
        server / "libraries/com/google/guava/guava/17.0/guava-17.0.jar",
    ]
    for path in [forge, minecraft, *dependencies]:
        if not path.is_file():
            raise FileNotFoundError(f"Missing GTNH 2.9.0 input: {path}")

    cache = ROOT / "build" / "tools"
    cache.mkdir(parents=True, exist_ok=True)
    ecj = get_tool("ecj.jar", cache)
    remapper = get_tool("specialsource.jar", cache)

    with TemporaryDirectory(dir=cache.parent) as temp:
        work = Path(temp)
        mappings = work / "forge.srg"
        with ZipFile(forge) as jar:
            raw = lzma.decompress(jar.read("deobfuscation_data-1.7.10.lzma"))
        # Keep the original package of the few classes outside the Minecraft mappings.
        mappings.write_bytes(b"\n".join(line for line in raw.splitlines() if not line.startswith(b"PK: ")) + b"\n")

        remapped = []
        for original in (minecraft, forge):
            target = work / original.name
            subprocess.run(
                ["java", "-jar", remapper, "-q", "-i", original, "-o", target, "-m", mappings],
                check=True,
            )
            remapped.append(target)

        classes = work / "classes"
        sources = sorted((ROOT / "src/main/java").rglob("*.java"))
        classpath = os.pathsep.join(str(path) for path in [*remapped, *dependencies])
        subprocess.run(
            ["java", "-jar", ecj, "-1.8", "-proc:none", "-classpath", classpath, "-d", classes, *sources],
            check=True,
        )

        output.parent.mkdir(parents=True, exist_ok=True)
        with ZipFile(output, "w", ZIP_DEFLATED) as jar:
            for directory in (ROOT / "src/main/resources", classes):
                for path in sorted(directory.rglob("*")):
                    if path.is_file():
                        jar.write(path, path.relative_to(directory))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("server", type=Path, help="Path to the extracted GTNH 2.9.0 Java 17–26 server")
    parser.add_argument("--output", type=Path, default=ROOT / "build/OffhandNoPlace-GTNH-1.0.2.jar")
    args = parser.parse_args()
    build(args.server.resolve(), args.output)
    print(args.output.resolve())


if __name__ == "__main__":
    main()
