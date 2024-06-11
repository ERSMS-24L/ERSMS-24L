package pl.edu.pw.ia.shared.adapters

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KeycloakService {
	@Value("\${kc.base:http://localhost:8080/keycloak}")
	private lateinit var keycloakAuthServerUrl: String
	@Value("\${kc.realm:ersms}")
	private lateinit var keycloakRealm: String
	@Value("\${kc.adminuser:admin}")
	private lateinit var adminUsername: String
	@Value("\${kc.adminpassword:admin}")
	private lateinit var adminPassword: String
	@Value("\${kc.masterclient:ersms_master_client}")
	private lateinit var masterClient: String

	fun getAccessToken(): String {
		val restTemplate = RestTemplate()
		val url = "$keycloakAuthServerUrl/realms/master/protocol/openid-connect/token"

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

		val map = LinkedMultiValueMap<String, String>()
		map.add("client_id", masterClient)
		map.add("grant_type", "password")
		map.add("scope", "openid")
		map.add("username", adminUsername)
		map.add("password", adminPassword)

		val request = HttpEntity(map, headers)

		val response = try {
			restTemplate.postForEntity(url, request, String::class.java)
		} catch (e: Exception) {
			throw RuntimeException("Failed to get access token")
		}

		if (response.statusCode == HttpStatus.OK) {
			val jsonObject = JSONObject(response.body)
			return jsonObject.getString("access_token")
		} else {
			throw RuntimeException("Failed to get access token")
		}
	}

	fun assignRoleToUser(userId: String, roleName: String) {
		val accessToken = getAccessToken()

		val restTemplate = RestTemplate()
		val url = "$keycloakAuthServerUrl/admin/realms/$keycloakRealm/users/$userId/role-mappings/realm"

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		headers.setBearerAuth(accessToken)

		var roleId = getRoleId(accessToken, roleName)
		if (roleId.isEmpty()) {
			createRole(accessToken, roleName)
			roleId = getRoleId(accessToken, roleName)
		}
		val roleJson = """[{"id": "$roleId", "name": "$roleName"}]"""

		val request = HttpEntity(roleJson, headers)

		val response = try {
			restTemplate.postForEntity(url, request, String::class.java)
		} catch (e: Exception) {
			throw RuntimeException("Failed to assign role to user")
		}

		if (response.statusCode != HttpStatus.NO_CONTENT) {
			throw RuntimeException("Failed to assign role to user")
		}
	}

	private fun createRole(accessToken: String, roleName: String) {
		val restTemplate = RestTemplate()
		val url = "$keycloakAuthServerUrl/admin/realms/$keycloakRealm/roles"

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		headers.setBearerAuth(accessToken)

		val role = JSONObject()
		role.put("name", roleName)

		val request = HttpEntity(role.toString(), headers)

		val response = try {
			restTemplate.postForEntity(url, request, String::class.java)
		} catch (e: Exception) {
			throw RuntimeException("Failed to create role")
		}

		if (response.statusCode != HttpStatus.CREATED) {
			throw RuntimeException("Failed to create role")
		}
	}


	private fun getRoleId(accessToken: String, roleName: String): String {
		val restTemplate = RestTemplate()
		val url = "$keycloakAuthServerUrl/admin/realms/$keycloakRealm/roles"

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		headers.setBearerAuth(accessToken)

		val request = HttpEntity<String>(headers)

		val response = try {
			restTemplate.exchange(url, HttpMethod.GET, request, String::class.java)
		} catch (e: Exception) {
			throw RuntimeException("Failed to get roles")
		}

		if (response.statusCode == HttpStatus.OK) {
			val rolesArray = JSONArray(response.body)
			for (i in 0 until rolesArray.length()) {
				val role = rolesArray.getJSONObject(i)
				if (roleName == role.getString("name")) {
					return role.getString("id")
				}
			}
			return "" // role not found
		} else {
			throw RuntimeException("Failed to get roles")
		}
	}
}