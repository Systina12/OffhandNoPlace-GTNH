# OffhandNoPlace-GTNH

适用于 Minecraft 1.7.10、GT New Horizons 2.9.0 和随包提供的 Backhand 1.8.18。

把 `OffhandNoPlace-GTNH-1.0.1.jar` 放进实例的 `mods/` 并重启。升级时用新 JAR 替换旧版本。多人联机时服务器和客户端都装。服务端负责拦截实际放置，客户端同步拦截本地的放置预演。

副手可以照常存放、显示和交换物品。常规方块、GregTech 机器、原版火把、红石粉、种子、门、床、告示牌、画、物品展示框和矿车在副手时无法放置。船、睡莲和原版水桶与岩浆桶的右键投放也会被拦截。AE2 部件和面板、ForgeMultipart 零件与微方块有独立覆盖。

1.0.1 增加了 BuildCraft 管道、Ender IO 导管和伪装板、匠魂熔融流体桶、可可豆种植和末影传送门框架的放置拦截。Ender IO 导管束内的安装和替换也会被拦截。骨粉施肥和投掷末影之眼走原有流程。

Backhand 在执行一次副手操作时临时选中副手槽。此 Mod 用 Backhand 公开的 `IBackhandPlayer.isUsingOffhand()` 判断当前操作，拦截位置在物品放置回调前。已有方块自身的交互，例如打开箱子，仍走原来的流程。食物的右键使用和空桶取流体也保留。自定义放置工具如果绕开这些回调，需对它的独立放置入口做适配。

此 Mod 通过独立 Mixin 加载，无需更换 Backhand 或修改其配置。移除 JAR 并重启即可恢复原行为。

## 从源码构建

需要 Python 3、Java 17 或以上，以及解压后的 [GTNH 2.9.0 Java 17–26 官方服务端包](https://downloads.gtnewhorizons.com/ServerPacks/GT_New_Horizons_2.9.0_Server_Java_17-26.zip)。构建脚本从 Maven Central 获取编译器和映射工具并校验 SHA-256。编译产物输出到 `build/OffhandNoPlace-GTNH-1.0.1.jar`，字节码目标为 Java 8。

```sh
python3 build.py /path/to/extracted/gtnh-server
```
