import axios from "axios";

const API_URL = 'http://localhost:9090/api'; // Your Gateway URL

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
   console.log("🔐 Token in request header:", token); 

    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    if (userId) {
        config.headers['X-User-ID'] = userId;
    }
    return config;
});

// --- Existing API Calls ---
export const getActivities = () => api.get('/activities');
export const addActivity = (activity) => api.post('/activities', activity);
export const getActivityDetail = (id) => api.get(`/recommendations/activity/${id}`);
export const getUserRecommendations = (userId) => api.get(`/recommendations/user/${userId}`); // Added for future use, if needed

// --- NEW API Calls for User Profile ---
export const getUserProfile = (keycloakId) => api.get(`/users/${keycloakId}`);
export const updateUserProfile = (keycloakId, profileData) => api.put(`/users/${keycloakId}/profile`, profileData);
// export const registerUser = (registerRequestData) => api.post(`/users/register`, registerRequestData);