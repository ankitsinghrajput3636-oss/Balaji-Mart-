package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTestLogic {

  @Test
  fun testDeliveryRadiusThreshold() {
    val storeDeliveryRadiusKm = 2.0
    val sector14Distance = 0.8
    val balajiChowkDistance = 0.3
    val outsideSector58Distance = 4.5

    assertTrue(sector14Distance <= storeDeliveryRadiusKm)
    assertTrue(balajiChowkDistance <= storeDeliveryRadiusKm)
    assertTrue(outsideSector58Distance > storeDeliveryRadiusKm)
  }

  @Test
  fun testCouponCalculation() {
    val subtotal = 250.0
    val discountAmount = 50.0
    val minOrder = 199.0

    val isValid = subtotal >= minOrder
    assertTrue(isValid)

    val finalSub = subtotal - discountAmount
    assertEquals(200.0, finalSub, 0.01)
  }

  @Test
  fun testFreeDeliveryThreshold() {
    val freeThreshold = 199.0
    val standardFee = 25.0

    val cartSmall = 150.0
    val feeSmall = if (cartSmall >= freeThreshold) 0.0 else standardFee
    assertEquals(25.0, feeSmall, 0.01)

    val cartLarge = 245.0
    val feeLarge = if (cartLarge >= freeThreshold) 0.0 else standardFee
    assertEquals(0.0, feeLarge, 0.01)
  }
}
