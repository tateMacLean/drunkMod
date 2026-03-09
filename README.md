# 🍺 DrunkMod — Fabric Mod for Minecraft 1.21.11

A Fabric mod that adds drinkable alcoholic beverages with pufferfish-style visual effects,
floating golden bubbles, and villager trades. No poison — just wobble!

---

## 📦 Requirements

| Tool | Version |
|------|---------|
| Minecraft | 1.21.11 |
| Fabric Loader | ≥ 0.18.1 |
| Fabric API | 0.141.3+1.21.11 |
| Java | 21 |

---

## 🔨 Building

```bash
./gradlew build
```

The compiled `.jar` will appear in `build/libs/drunkmod-1.0.0.jar`.  
Copy it to your `.minecraft/mods/` folder alongside Fabric API.

---

## 🍻 Drinks

| Item | Duration | Effect Level | Crafting |
|------|----------|-------------|---------|
| **Beer** | 60s | Tipsy (I) | Glass Bottle + 2x Wheat + Sugar |
| **Mead** | 80s | Drunk (II) | Glass Bottle + Honeycomb + Sugar + Water Bucket |
| **Wine** | 100s | Drunk (II) | Glass Bottle + 3x Sweet Berries + Sugar |
| **Rum** | 120s | Very Drunk (III) | Glass Bottle + 3x Sugar + Blaze Powder |
| **Whiskey** | 150s | Wasted (IV) | 2x Beer + 2x Blaze Powder |

> **Stacking:** Drinking multiple bottles stacks both the duration and amplifier (up to level IV).
> Empty glass bottles are returned to your inventory after drinking.

---

## 🌀 Drunk Effect

The `Drunk` status effect causes:

- **Screen wobble** — same waving distortion as eating a pufferfish, but **without poison**
  - Level I (Tipsy): subtle wobble
  - Level II (Drunk): noticeable sway
  - Level III (Very Drunk): heavy distortion
  - Level IV (Wasted): maximum chaos

- **Golden bubbles** — float upward around the drunk player, visible to all nearby players.
  Higher amplifiers produce more frequent and numerous bubbles.

- **Stumbling** — at level III+, the player occasionally gets a random velocity nudge.

---

## 🏘️ Villager Trades

Drinks are available from villagers at various profession tiers:

| Villager | Tier | Trade |
|----------|------|-------|
| Farmer | 1 (Novice) | 2 emeralds → 3 Beers |
| Farmer | 2 (Apprentice) | 3 emeralds → 2 Meads |
| Butcher | 3 (Journeyman) | 4 emeralds → 2 Wines |
| Cleric | 4 (Expert) | 6 emeralds → 1 Rum |
| Cleric | 5 (Master) | 10 emeralds → 1 Whiskey |

---

## 📁 Project Structure

```
src/
├── main/java/com/drunkmod/
│   ├── DrunkMod.java              # Main entrypoint
│   ├── DrunkTickHandler.java      # Spawns bubbles around drunk players
│   ├── effect/
│   │   ├── DrunkEffect.java       # Status effect logic
│   │   └── ModEffects.java        # Effect registry
│   ├── item/
│   │   ├── AlcoholicDrinkItem.java  # Base drink item class
│   │   └── ModItems.java           # Item registry (Beer, Mead, Wine, Rum, Whiskey)
│   ├── particle/
│   │   └── ModParticles.java       # Particle type registry
│   └── trade/
│       └── DrunkModTrades.java     # Villager trade registration
│
├── client/java/com/drunkmod/
│   ├── DrunkModClient.java         # Client entrypoint
│   ├── mixin/
│   │   ├── GameRendererMixin.java  # Injects drunk wobble into renderer
│   │   └── InGameHudMixin.java     # Removes nausea/portal overlays
│   └── particle/
│       └── DrunkBubbleParticle.java # Golden bubble particle renderer
│
└── main/resources/
    ├── fabric.mod.json
    ├── drunkmod.client.mixins.json
    ├── assets/drunkmod/
    │   ├── lang/en_us.json
    │   ├── models/item/{beer,mead,wine,rum,whiskey}.json
    │   ├── particles/drunk_bubble.json
    │   └── textures/
    │       ├── item/               ← Replace with your own 16x16 PNGs
    │       └── effect/drunk.png
    └── data/drunkmod/recipe/       ← Crafting recipes
```

---

## 🎨 Custom Textures

The included textures are simple placeholder sprites. To replace them:

1. Create 16×16 pixel PNG files in the style of Minecraft's potion bottle icons.
2. Drop them in `src/main/resources/assets/drunkmod/textures/item/`.
3. Files needed: `beer.png`, `mead.png`, `wine.png`, `rum.png`, `whiskey.png`.
4. For the HUD effect icon: `textures/effect/drunk.png` (18×18).

---

## ⚙️ Extending the Mod

### Adding a new drink

1. Add a new field in `ModItems.java`:
```java
public static final AlcoholicDrinkItem MY_DRINK = new AlcoholicDrinkItem(
    new Item.Settings(), 2000, 2  // duration ticks, amplifier
);
```

2. Register it in `registerItems()`:
```java
register("my_drink", MY_DRINK);
```

3. Add a texture at `assets/drunkmod/textures/item/my_drink.png`
4. Add a model at `assets/drunkmod/models/item/my_drink.json`
5. Add a lang entry in `en_us.json`
6. Optionally add a crafting recipe in `data/drunkmod/recipe/my_drink.json`

---

## 📜 License

MIT — free to use, modify, and distribute.
