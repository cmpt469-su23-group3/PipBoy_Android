package com.example.pipboyv1.util

import java.util.UUID


fun String.toUUID() = UUID.fromString(this)
