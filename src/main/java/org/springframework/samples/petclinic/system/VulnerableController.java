/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VulnerableController {

	@GetMapping("/vulnerable/read-file")
	public String readFile(@RequestParam String filename) throws Exception {
		// Vulnerability: path traversal via unsanitized filename input
		Path path = Paths.get("src/main/resources", filename);
		return Files.readString(path);
	}

	@GetMapping("/vulnerable/ping")
	public String ping(@RequestParam String host) throws Exception {
		// Vulnerability: command injection via unsanitized host input
		String command = "ping -c 1 " + host;
		Process process = Runtime.getRuntime().exec(command);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append('\n');
			}
			process.waitFor();
			return output.toString();
		}
	}
}
