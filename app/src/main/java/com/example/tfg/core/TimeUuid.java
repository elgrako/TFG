package com.example.tfg.core;

import java.time.Instant;
import java.util.UUID;

public class TimeUuid {
    public static String uuid() { return UUID.randomUUID().toString(); }
    public static String nowIso() { return Instant.now().toString(); }
}
