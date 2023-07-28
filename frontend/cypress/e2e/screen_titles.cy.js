// Copyright 2023 DDS Permissions Manager Authors
/// <reference types="Cypress" />

describe('title should say DDS Permissions Manager', () => {
    beforeEach(() => {
        cy.login('unity-admin', 'password');
        cy.intercept('http://localhost:8080/api/token_info').as('tokenInfo');
        cy.visit('http://localhost:8080/');
        cy.wait('@tokenInfo');
    });

    it('should have correct titles', () => {
        cy.get('title')
        .invoke('text')
        .should('equal', 'My Groups | DDS Permissions Manager');


        cy.get('h1')
        .invoke('text')
        .should('equal', 'My Groups');


        cy.visit('/users');

        cy.get('[data-cy="users"]')
        .invoke('text')
        .should('equal', 'My Users');

        cy.get('[data-cy="super-users"]')
        .invoke('text')
        .should('equal', 'My Super Users');


        cy.visit('/topics');

        cy.get('[data-cy="topics"]')
        .invoke('text')
        .should('equal', 'My Topics');


        cy.visit('/applications');

        cy.get('[data-cy="applications"]')
        .invoke('text')
        .should('equal', 'My Applications');
        
    });
});
