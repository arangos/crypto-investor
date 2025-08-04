package com.codebulls.criptoinvestor.model.dto

import com.codebulls.criptoinvestor.constants.SignalType
import java.time.Instant

data class MaSignal (
    val timestamp: Instant,
    val signal: SignalType
)