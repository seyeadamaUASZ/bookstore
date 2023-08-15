package com.sid.gl.controllers;

import com.sid.gl.services.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.togglz.core.manager.FeatureManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private FeatureManager manager;
    @Mock
    private IBookService iBookService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createBook() {
    }

    @Test
    void getBooks() {
    }

    @Test
    void getBook() {
    }
}