[1.1.5]

**Fixed**
* Floating Pools now properly detect the actual water surface instead of relying on the raw world-surface heightmap. This also works with lily pads, flowers and other thin plants placed on top of the water by other mods, preventing debris from spawning noticeably above the water
* Ponds and oases are no longer excluded just because their biome isn't tagged as a river or ocean. Any actual body of water can now be used as a valid spawn location
* Spawn checks now pick a fresh nearby location instead of reusing the same cached position for two consecutive checks, making spawns feel more natural
* Added proper clearance checks at the selected spawn position, preventing Floating Pools from spawning directly on top of or inside each other

**Added**
* Fishing Net now has 64 durability, worn down by 1 each time a catch is retrieved, and can be repaired with String on an anvil

***

[1.1.4]

**Fixed**
* Floating Books fishing loot pool dropped enchanted books at ~1% instead of the intended rate after the enchanted book entry lost its loot weight during the 1.21.1 data conversion; rebalanced so enchanted books now show up as a proper, exciting catch (~15%) without drowning out Book and Quill
* Redstone Coil could spawn its unbreakable top half just by being looked at (no placement input), and the ghost half would reappear immediately after breaking it, because `getStateForPlacement` placed the top half as a side effect instead of only doing so once placement was confirmed
* Hanging Frame did not drop its item when broken from the top half
* Hanging Frame only ever had one shared row of 3 display slots rendered inside the solid lower crossbar instead of on its two actual display panels; it now has 6 slots (3 per half) so the top and bottom of the frame display fish independently, each rendered on its own panel
* Fish Trophy Frame spawned a duplicate, client-only item entity when broken while displaying an item, causing a visual item duplication/desync
* Fish Trap silently destroyed a completed catch instead of dropping it when the output slot already held a different item
* Hanging Frames placed before the 6-slot fix stayed stuck at 3 slots forever after a world reload, since the saved slot count was loaded back verbatim instead of only ever growing

**Added**
* Japanese (ja_jp) translation (thanks to Anpan715)
* Redstone Coil now plays a click when its target mode is switched (pitch rises with each mode) and an activate/deactivate sound when it gets powered or unpowered by redstone
* Redstone Coil's attack beam and impact are much more intense: the bolt is now a thick, crackling multi-strand arc instead of a single drifting spark, with an occasional flash strobe along its path and a bigger flash/explosion/spark burst on impact
* Hanging Frame can now be filled/emptied from either side (e.g. a north-facing frame also accepts interaction from the south, east from the west, etc.), not just from the front, with left/right correctly matching the same slots from both sides instead of being mirrored

***

[1.1.3]

**Fixed**
* Hanging Frame could place its top half without proper interaction and spawn blocks automatically in certain situations (thanks to voxu-git)

**Changed**
* Floating world entities (Floating Debris, Floating Books, River Fish Pool, Ocean Fish Pool) are now registered as MISC instead of CREATURE

***

[1.1.2-fabric]

* Crash on Fabric client startup caused by missing refmap in mixin

***

[1.1.2]

* Crash when ticking Fish Trap: `NoSuchElementException` in `FishTrapBlockEntity.getRecipe`
  is eliminated by guarding empty recipe lookups and null-guarding `tick()`. 
* Fish Trap interactions: taking items could duplicate and `FULL` would not reset.

***

[1.1.1]

**Fixed**
* Fishing Net can now be emptied with right-click when full
* Floating Debris now correctly spawns again on NeoForge
* Fish Trophy Frame now properly updates and clears its renderer when items are removed
* Display state synced via new blockstate property instead of packet updates

**Changed**
* `placed_block` criteria additionally require the matching BlockItem via `"item": { "items": [...] }`.
* Spyglass-at-floating-debris uses a valid item icon.

**Added**
* Optional item tag `#lilis_lucky_lures:advancement_starters` to gate the root unlock.

***

[1.1.0]

** Ported to 1.21.1 **

***

[1.0.2]

**Added**
-

**Changed**
* Removed the not used 'use pufferfish on fish trophy frame' advancement
* BambooFishingRod has now a Durability of 32
* If youre Inventory is full while your opening a Soaked Bag the new content will be dropped
* FishingNet now only stacks to 1 

**Fixed**
* Redstone Coil & Fish Frame Block now correctly dropping when mining the top part
* Content from FishTrapBlock not dropping after breaking it 
* Content from Fish Frame not dropping after breaking it 
* Food now properly restores nutrition and saturation

***

[1.0.1] 

**Added**
-

**Changed**
* FishTrap Animation 

**Fixed**
* Forge crashing when creating a new World
