package com.acme.cxf;

import com.acme.cxf.api.HelloService;
import com.acme.cxf.impl.HelloServiceImpl;
import com.acme.cxf.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelloServiceTest {
    
    private HelloService service;

    @BeforeEach
    public void setUp() {
        // Créer une instance du service
        service = new HelloServiceImpl();
    }

    @Test
    public void testSayHello() {
        String result = service.sayHello("Alice");
        assertNotNull(result);
        assertTrue(result.contains("Bonjour"));
        assertTrue(result.contains("Alice"));
    }

    @Test
    public void testSayHelloWithNull() {
        String result = service.sayHello(null);
        assertNotNull(result);
        assertTrue(result.contains("inconnu"));
    }

    @Test
    public void testFindPersonById() {
        Person person = service.findPersonById("123");
        assertNotNull(person);
        assertEquals("123", person.getId());
        assertEquals("Ada Lovelace", person.getName());
        assertEquals(36, person.getAge());
    }

    @Test
    public void testPersonSerialization() {
        Person person = new Person("1", "Bob", 25);
        assertEquals("1", person.getId());
        assertEquals("Bob", person.getName());
        assertEquals(25, person.getAge());
    }
}
