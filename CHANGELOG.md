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
