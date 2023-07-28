// Copyright 2023 DDS Permissions Manager Authors
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

Cypress.Commands.add('login', (username, password) => {
    cy.request('POST', 'http://localhost:8080/api/login', {
        username: username,
        password: password,
    });
})

Cypress.Commands.add('assertClipboard', (expectedText) => {
    cy.window().then((win) => {
      if (!win.navigator.clipboard.writeText.__cypress_stub__) {
        cy.stub(win.navigator.clipboard, 'writeText').as('clipboardWriteText');
      }
    });
    cy.get('@clipboardWriteText').should('be.calledWith', expectedText);
  });
  
  
