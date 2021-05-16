package hu.bme.mit.spaceship;

import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  @BeforeEach
  public void init(){

    this.primaryTorpedoStore = mock(TorpedoStore.class, withSettings().useConstructor(10));
    this.secondaryTorpedoStore = mock(TorpedoStore.class, withSettings().useConstructor(10));
    this.ship = new GT4500(this.primaryTorpedoStore, this.secondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(this.primaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){

    // Arrange
    when(this.primaryTorpedoStore.isEmpty()).thenReturn(false).thenReturn(true).thenReturn(true);
    when(this.secondaryTorpedoStore.isEmpty()).thenReturn(false).thenReturn(false).thenReturn(true);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(this.primaryTorpedoStore, times(1)).fire(1);
    verify(this.secondaryTorpedoStore, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_Primary(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(false);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(0).thenReturn(0);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
  }

  @Test
  public void fireTorpedo_Single_Two_Times(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(0);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result1);
    assertTrue(result2);
  }

  @Test
  public void fireTorpedo_Single_Primary_Two_Times(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true).thenReturn(true);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(false);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(2).thenReturn(1).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(0).thenReturn(0);
    when(this.secondaryTorpedoStore.isEmpty()).thenReturn(true);

    int initialPrimary = this.primaryTorpedoStore.getTorpedoCount();
    int initialSecondary = this.secondaryTorpedoStore.getTorpedoCount();

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result1);
    assertTrue(result2);
  }

  @Test
  public void fireTorpedo_Single_Secondary_Two_Times(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(false).thenReturn(false);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true).thenReturn(true);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(0).thenReturn(0).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(2).thenReturn(1).thenReturn(0);
    when(this.secondaryTorpedoStore.isEmpty()).thenReturn(false).thenReturn(false).thenReturn(false);
    when(this.primaryTorpedoStore.isEmpty()).thenReturn(true).thenReturn(true).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result1);
    assertTrue(result2);
  }

  @Test
  public void fireTorpedo_Single_Failure_Report(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(false).thenReturn(false);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true).thenReturn(true);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(1);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(1);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_All_With_Empty_Stores(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(false).thenReturn(false);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(false).thenReturn(false);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(0).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(0).thenReturn(0);
    when(this.primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(this.secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertFalse(result);
  }

  @Test
  public void fireTorpedo_Single_Check_Types(){
    // Arrange
    when(this.primaryTorpedoStore.fire(1)).thenReturn(true).thenReturn(false);
    when(this.secondaryTorpedoStore.fire(1)).thenReturn(true).thenReturn(false);
    when(this.primaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(0);
    when(this.secondaryTorpedoStore.getTorpedoCount()).thenReturn(1).thenReturn(0);
    when(this.primaryTorpedoStore.isEmpty()).thenReturn(false).thenReturn(false);
    when(this.secondaryTorpedoStore.isEmpty()).thenReturn(false).thenReturn(false);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean wasprimary1 = ship.wasPrimaryFiredLast();

    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean wasprimary2 = ship.wasPrimaryFiredLast();

    // Assert
    assertTrue(wasprimary1);
    assertTrue(result1);
    assertFalse(wasprimary2);
    assertTrue(result2);
  }

  @Test
  public void FireLaser_Failure(){
    // Arrange

    // Act
    boolean result = ship.fireLaser(FiringMode.SINGLE);

    // Assert
    assertFalse(result);
  }


  @Test
  public void fireTorpedo_Single_Success_With_Deafult_Stores(){

    // Arrange
    SpaceShip ship = new GT4500();

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertTrue(result);
  }




}
