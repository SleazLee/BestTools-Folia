# Todo
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/Block.html#getDrops(org.bukkit.inventory.ItemStack,org.bukkit.entity.Entity)
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/Block.html#isPreferredTool(org.bukkit.inventory.ItemStack)
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/Block.html#getBreakSpeed(org.bukkit.entity.Player)

## Updating to Folia compatible BestTools

This fork introduces support for [Folia](https://github.com/PaperMC/Folia) so the plugin can run on asynchronous servers. A custom `Scheduler` class automatically chooses between Folia schedulers and the Bukkit scheduler at runtime. All internal tasks now rely on this helper to remain compatible with both server types.

Additional changes include:

- **Paper API** – the project now depends on the Paper API instead of Spigot, providing Folia-ready scheduler classes.
- **Scheduler integration** – `BestToolsListener`, `GUIHandler` and `RefillUtils` were updated to schedule their actions through the new `Scheduler` class.
- **New file** – `src/main/java/de/jeff_media/BestTools/Scheduler.java` contains the implementation of the cross-compatible scheduler.

Server owners upgrading should update their dependency to the Paper repository. Existing configurations remain the same, and the plugin will automatically switch scheduling implementations depending on the runtime environment.
