import React from 'react';
import ReactDOM from 'react-dom/client';

import { Provider } from 'react-redux';
import { store } from './store/store';

import App from './App';
import { AuthProvider } from 'react-oauth2-code-pkce';
import { authConfig } from './authConfig';
import { BrowserRouter } from 'react-router-dom'; // ✅ Import added

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <AuthProvider authConfig={authConfig} loadingComponent={<div>Loading...</div>}>
    <Provider store={store}>
      <BrowserRouter> {/* ✅ Wrap App inside BrowserRouter */}
        <App />
      </BrowserRouter>
    </Provider>
  </AuthProvider>
);
