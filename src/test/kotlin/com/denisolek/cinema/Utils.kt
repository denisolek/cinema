package com.denisolek.cinema

import arrow.core.Either

val <A, B> Either<A, B>.rightValue: B get() = (this as Either.Right).value
val <A, B> Either<A, B>.leftValue: A get() = (this as Either.Left).value