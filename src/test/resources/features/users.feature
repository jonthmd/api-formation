Feature: Gestion des utilisateurs

  En tant que consommateur de l'API
  Je souhaite consulter les utilisateurs
  Afin de récupérer leurs informations

  Scenario: Consulter la liste des utilisateurs
    Given je prépare une requête API
    When j'appelle GET "/api/users"
    Then le statut HTTP doit être 200
    And la réponse contient 2 utilisateurs

  Scenario: Consulter un utilisateur inexistant
    Given je prépare une requête API
    When j'appelle GET "/api/users/999"
    Then le statut HTTP doit être 404

  Scenario: Créer un utilisateur
    Given je prépare une requête API
    When je crée un utilisateur avec le nom "Charlie" et l'email "charlie@test.com"
    Then le statut HTTP doit être 201
    And l'utilisateur créé possède un identifiant

  Scenario: Supprimer un utilisateur
    Given je prépare une requête API
    When je supprime l'utilisateur avec l'identifiant 2
    Then le statut HTTP doit être 204

  Scenario: Vérifier qu'un utilisateur supprimé n'existe plus
    Given je prépare une requête API
    And je crée un utilisateur avec le nom "Charlie" et l'email "charlie@test.com"
    And je supprime l'utilisateur créé
    When j'appelle GET "/api/users/{id}"
    Then le statut HTTP doit être 404