package com.example.actionsfabric;

import net.fabricmc.api.ClientModInitializer;

public class ActionsClient implements ClientModInitializer {
    public static boolean IDLE_BREATHING = true;
    public static boolean WALK_SWAY = true;
    public static boolean SPRINT_LEAN = true;
    public static boolean SWIM_CRAWL = true;
    public static boolean SNEAK_POSE = true;

    @Override
    public void onInitializeClient() {
        System.out.println("[Actions Fabric] loaded");
    }
}
