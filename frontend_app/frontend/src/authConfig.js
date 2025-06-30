export const authConfig = {
    clientId: 'oauth2-pkce-client',
    authorizationEndpoint: `${import.meta.env.VITE_KEYCLOAK_URL}/realms/fitness-oauth2/protocol/openid-connect/auth`,
    tokenEndpoint: `${import.meta.env.VITE_KEYCLOAK_URL}/realms/fitness-oauth2/protocol/openid-connect/token`,
    redirectUri: import.meta.env.VITE_REDIRECT_URI,
    scope: 'openid profile email offline_access',
    logoutUrl: `${import.meta.env.VITE_KEYCLOAK_URL}/realms/fitness-oauth2/protocol/openid-connect/logout`,
    logoutRedirectUri: import.meta.env.VITE_REDIRECT_URI,
    onRefreshTokenExpire: (event) => event.logIn(),
    autoLogin: false,
};
