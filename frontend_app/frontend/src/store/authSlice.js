import { createSlice } from '@reduxjs/toolkit'

const authSlice = createSlice({
  name: 'auth',
  initialState : {
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    userId: localStorage.getItem('userId') || null
  },
  reducers: {
    // setCredentials: (state, action) => {
    //   state.user = action.payload.user;
    //   state.token = action.payload.token;
    //   state.userId = action.payload.user.sub;

    //   localStorage.setItem('token', action.payload.token);
    //   localStorage.setItem('user', JSON.stringify(action.payload.user));
    //   localStorage.setItem('userId', action.payload.user.sub);
    // },
    setCredentials: (state, action) => {
  state.user = action.payload.keycloakUser;
  state.token = action.payload.token;
  state.userId = action.payload.keycloakUser.sub;

  localStorage.setItem('token', action.payload.token);
  localStorage.setItem('user', JSON.stringify(action.payload.keycloakUser));
  localStorage.setItem('userId', action.payload.keycloakUser.sub);
},

     // NEW: Action to store the full user profile fetched from your UserService backend
    setUserProfile: (state, action) => {
            state.user = action.payload; // action.payload will be the UserResponse from backend
            localStorage.setItem('user', JSON.stringify(action.payload)); // Store the full backend user profile
            state.userProfileFetched = true; // Mark as fetched
        },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.userId = null;
       state.userProfileFetched = false;
      
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('userId');
    },
  },
});

export const { setCredentials, setUserProfile, logout } = authSlice.actions;
export default authSlice.reducer;