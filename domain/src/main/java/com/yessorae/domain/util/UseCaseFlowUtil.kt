package com.yessorae.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip

fun <T1, T2, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    transform: suspend (T1, T2) -> R
): Flow<R> =
    first.zip(second) { a, b -> transform(a, b) }

fun <T1, T2> zip(
    first: Flow<T1>,
    second: Flow<T2>,
): Flow<Pair<T1, T2>> =
    first.zip(second) { a, b -> a to b }

fun <T1, T2, T3, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    transform: suspend (T1, T2, T3) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c ->
            transform(a, b, c)
        }

fun <T1, T2, T3, T4, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    fourth: Flow<T4>,
    transform: suspend (T1, T2, T3, T4) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c -> Triple(a, b, c) }
        .zip(fourth) { (a, b, c), d -> transform(a, b, c, d) }

fun <T1, T2, T3, T4, T5, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    fourth: Flow<T4>,
    fifth: Flow<T5>,
    transform: suspend (T1, T2, T3, T4, T5) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c -> Triple(a, b, c) }
        .zip(fourth) { (a, b, c), d -> Pair(a, b) to Pair(c, d) }
        .zip(fifth) { a, b -> transform(a.first.first, a.first.second, a.second.first, a.second.second, b) }

fun <T1, T2, T3, T4, T5, T6, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    fourth: Flow<T4>,
    fifth: Flow<T5>,
    sixth: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c -> Triple(a, b, c) }
        .zip(fourth) { (a, b, c), d -> Pair(a, b) to Pair(c, d) }
        .zip(fifth) { (a, b), c -> Triple(a, b, c) }
        .zip(sixth) { a, b -> transform(a.first.first, a.first.second, a.second.first, a.second.second, a.third, b) }

fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple)
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple),
    flow7
) { t1, t2, t3 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third,
        t3
    )
}
