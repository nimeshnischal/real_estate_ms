package com.nimesh.real_estate_ms;

import com.nimesh.real_estate_ms.controller.PropertyController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.*;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RealEstateMsApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	PropertyController propertyController;

	@Test
	public void contextLoads() {
		assertThat(propertyController).isNotNull();
		assertNotEquals(port, 0);
	}

}
