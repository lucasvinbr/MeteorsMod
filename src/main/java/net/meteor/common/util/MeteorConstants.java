package net.meteor.common.util;

import net.minecraft.util.DamageSource;

public class MeteorConstants {

    public static final int MAXIMUM_LIGHT_VALUE = 15;
    public static final String PICKAXE_TOOL_ID = "pickaxe";
    public static final int MAX_METEOR_HEAT = 6;

    public static final class DamageSources {

        public static final DamageSource FROZEN_FLOOR = new DamageSource("frozen_floor").setMagicDamage();
    }

    public static final class MeteorTimer {

        public static final String METER_TIMER_MODE = "mode";
        public static final int MAX_TIMER_POWER = 15;
    }

    public static final class MeteorTools {
        public static final float METEORITE_AXE_DAMAGE = 8f;
        public static final float METEORITE_AXE_SPEED = -3.1f;

        public static final float FREZARITE_AXE_DAMAGE = 8f;
        public static final float FREZARITE_AXE_SPEED = -3.1f;
    }

    public class Slippery {

        public static final String FACADE_BLOCK_KEY = "facade";

    }
}
