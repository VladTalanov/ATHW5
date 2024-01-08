package org.max.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Демонстрация работы библиотеки мокирования
 */
@ExtendWith(MockitoExtension.class)
public class MockitoExampleTest {

    @Mock
    HashMap<String, String> hashMapMock = new HashMap<>();

    //Работа с mock объектами
    @Test
    void simpleMockTest() {

        List mockList = Mockito.mock(ArrayList.class);

        mockList.add("one");
        Mockito.verify(mockList).add("one");
        assertEquals(0, mockList.size());

        Mockito.when(mockList.size()).thenReturn(100);
        assertEquals(100, mockList.size());

        hashMapMock.put("one", "two");
        Mockito.verify(hashMapMock, Mockito.times(1)).put("one", "two");
        Mockito.when(hashMapMock.size()).thenReturn(200);
        assertEquals(200, hashMapMock.size());
    }

    @Spy
    HashMap<String, String> hashMapSpy = new HashMap<>();

    //Демонстрация работы со SPY объектами
    @Test
    void simpleSpyTest() {

        List mockList = Mockito.spy(ArrayList.class);

        mockList.add("one");
        Mockito.verify(mockList).add("one");
        assertEquals(1, mockList.size());

        Mockito.when(mockList.size()).thenReturn(100);
        assertEquals(100, mockList.size());

        hashMapSpy.put("one", "two");
        Mockito.verify(hashMapSpy, Mockito.times(1)).put("one", "two");
        assertEquals(1, hashMapSpy.size());
        Mockito.when(hashMapSpy.size()).thenReturn(200);
        assertEquals(200, hashMapSpy.size());
    }
}
