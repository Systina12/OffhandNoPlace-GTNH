# OffhandNoPlace-GTNH

适用于 Minecraft 1.7.10、GT New Horizons 2.9.0 和随包提供的 Backhand 1.8.18。

把 `OffhandNoPlace-GTNH-1.0.2.jar` 放进客户端实例的 `mods/` 并重启。升级时用新 JAR 替换旧版本。单机和联机都可以只装客户端。服务端也可以安装，为全服玩家执行放置限制。

副手可以照常存放、显示和交换物品。常规方块、GregTech 机器、原版火把、红石粉、种子、门、床、告示牌、画、物品展示框和矿车在副手时无法放置。船、睡莲和原版水桶与岩浆桶的右键投放也会被拦截。AE2 部件和面板、ForgeMultipart 零件与微方块有独立覆盖。

1.0.1 增加了 BuildCraft 管道、Ender IO 导管和伪装板、匠魂熔融流体桶、可可豆种植和末影传送门框架的放置拦截。Ender IO 导管束内的安装和替换也会被拦截。骨粉施肥和投掷末影之眼走原有流程。

Backhand 在执行一次副手操作时临时选中副手槽。此 Mod 用 Backhand 公开的 `IBackhandPlayer.isUsingOffhand()` 判断当前操作。1.0.2 提前到客户端发包之前拦截副手放置，主手仍可打开箱子等已有方块。副手的食物右键和空桶取流体保留。安装在服务端时，服务端也会拦截这些物品的放置回调。自定义放置工具如果走独立入口，需要单独适配。

此 Mod 通过独立 Mixin 加载，无需更换 Backhand 或修改其配置。移除 JAR 并重启即可恢复原行为。

## 从源码构建

需要 Python 3、Java 17 或以上，以及解压后的 [GTNH 2.9.0 Java 17–26 官方服务端包](https://downloads.gtnewhorizons.com/ServerPacks/GT_New_Horizons_2.9.0_Server_Java_17-26.zip)。构建脚本从 Maven Central 获取编译器和映射工具并校验 SHA-256。编译产物输出到 `build/OffhandNoPlace-GTNH-1.0.2.jar`，字节码目标为 Java 8。

```sh
python3 build.py /path/to/extracted/gtnh-server
```
