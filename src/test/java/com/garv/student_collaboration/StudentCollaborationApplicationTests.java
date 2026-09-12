package com.garv.student_collaboration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"app.jwt.secret=c3ByaW5nLWJvb3QtdGVzdC1qd3Qtc2VjcmV0LWtleS0zMi1ieXRlcw==",
		"app.jwt.expiration-ms=3600000"
})
class StudentCollaborationApplicationTests {

	@Test
	void contextLoads() {
	}

}
