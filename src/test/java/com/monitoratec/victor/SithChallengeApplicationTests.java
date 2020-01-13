package com.monitoratec.victor;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class SithChallengeApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue();
	}

}
