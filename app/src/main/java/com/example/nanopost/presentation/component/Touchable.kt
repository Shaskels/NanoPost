package com.example.nanopost.presentation.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.unit.IntSize
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

//fun Modifier.touchable(
//    onClick: (Offset) -> Unit
//): Modifier {
//    return this.then(TouchableElement(onClick))
//}
//
//class TouchableElement internal constructor(
//    private var onClick: (Offset) -> Unit,
//) : ModifierNodeElement<TouchableNode>() {
//    override fun create(): TouchableNode {
//        return TouchableNode(onClick)
//    }
//
//    override fun update(node: TouchableNode) {
//        node.onClick = onClick
//    }
//
//    override fun hashCode(): Int {
//        return onClick.hashCode()
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return other is TouchableElement && onClick == other.onClick
//    }
//
//}
//
//class TouchableNode(
//    var onClick: (Offset) -> Unit,
//) : Modifier.Node(), PointerInputModifierNode, LayoutAwareModifierNode {
//
//    private var currentCoordinates: LayoutCoordinates? = null
//    private var lastPointerEvent: PointerEvent? = null
//    private var lastEventTime: Long = 0L
//
//    @OptIn(ExperimentalTime::class)
//    override fun onPointerEvent(
//        pointerEvent: PointerEvent,
//        pass: PointerEventPass,
//        bounds: IntSize
//    ) {
//        if (pass != PointerEventPass.Main) return
//        when (pointerEvent.type) {
//            PointerEventType.Press -> {
//                lastPointerEvent = pointerEvent
//                lastEventTime = Clock.System.now().toEpochMilliseconds()
//            }
//
//            PointerEventType.Move -> {
//                lastPointerEvent = null
//            }
//
//            PointerEventType.Release if lastPointerEvent != null && pointerEvent.changes.none -> {
//                val previousEventTime = lastEventTime
//                if (Clock.System.now().toEpochMilliseconds() - previousEventTime < 200) {
//                    val windowCoordinates =
//                        currentCoordinates?.localToWindow(pointerEvent.changes.first().position)
//                            ?: return
//                    onClick(pointerEvent.changes.first().position)
//                    pointerEvent.changes.forEach { it.consume() }
//                }
//                lastPointerEvent = null
//            }
//
//            else -> Unit
//        }
//    }
//
//    override fun onCancelPointerInput() {
//        lastPointerEvent = null
//    }
//
//    override fun onPlaced(coordinates: LayoutCoordinates) {
//        currentCoordinates = coordinates
//    }
//
//}