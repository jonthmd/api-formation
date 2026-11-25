
package com.formation.api.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long createdUserId;

    @Given("je prépare une requête API")
    public void prepareRequest() {
        // Le port est injecté automatiquement par Spring.
    }

    @When("j'appelle GET {string}")
    public void callGet(String endpoint) {

        if (endpoint.contains("{id}")) {
            endpoint = endpoint.replace(
                    "{id}",
                    String.valueOf(createdUserId)
            );
        }

        response = given()
                .port(port)
                .when()
                .get(endpoint);
    }

    @Then("le statut HTTP doit être {int}")
    public void verifyStatus(int expectedStatus) {

        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
    }

    @Then("la réponse contient {int} utilisateurs")
    public void verifyUsersCount(int expectedCount) {

        int actualCount = response.jsonPath()
                .getList("$")
                .size();

        assertThat(actualCount).isEqualTo(expectedCount);
    }

    @When("je crée un utilisateur avec le nom {string} et l'email {string}")
    public void createUser(String name, String email) {

        HashMap<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);

        response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/users");

        createdUserId = response.jsonPath().getLong("id");
    }

    @And("l'utilisateur créé possède un identifiant")
    public void verifyCreatedUserId() {

        Long id = response.jsonPath().getLong("id");

        assertThat(id).isNotNull().isPositive();
    }

    @When("je supprime l'utilisateur avec l'identifiant {long}")
    public void deleteUser(long id) {

        response = given()
                .port(port)
                .when()
                .delete("/api/users/" + id);
    }

    @When("je supprime l'utilisateur créé")
    public void deleteCreatedUser() {

        deleteUser(createdUserId);
    }
}
