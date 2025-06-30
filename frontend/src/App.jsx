import { Box, Button, Typography, CircularProgress } from "@mui/material"; // Added CircularProgress
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch, useSelector } from "react-redux"; // Added useSelector
import { useNavigate, useLocation, Navigate, Routes, Route } from 'react-router-dom'; // ✅ from 'react-router-dom'

import { setCredentials, setUserProfile,logout as logoutAction } from "./store/authSlice"; // Import setUserProfile
import UserProfileDisplay from "./componenets/UserProfileDisplay";
import ActivityForm from "./componenets/ActivityForm"; // Corrected typo: components
import ActivityList from "./componenets/ActivityList";
import ActivityDetail from "./componenets/ActivityDetail"; // Corrected typo: components
import ProfileCompletionForm from "./componenets/ProfileCompletionForm";
// import WelcomePage from "./componenets/WelcomePage";
import { getUserProfile } from "./services/api"; // <<-- Using getUserProfile
import WelcomePage from "./componenets/WelcomePage";




const ActvitiesPage = () => {
    // Access userProfile from Redux state here
    const userProfile = useSelector((state) => state.auth.user);
    

    return (
        <Box sx={{ p: 2, border: '1px dashed grey' }}>
            <UserProfileDisplay /> {/* Display user profile summary */}
            <ActivityForm onActivityAdded={() => window.location.reload()} />
            <ActivityList />
        </Box>
    );
};


// function App() {
//     const { token, tokenData, logIn, isAuthenticated ,logOut} = useContext(AuthContext);
//     const dispatch = useDispatch();
//     // Use Redux state to track if the full user profile has been fetched
//     const userProfile = useSelector((state) => state.auth.user);
//     const userId = useSelector((state) => state.auth.userId); // Get Keycloak ID from Redux
//     const userProfileFetched = useSelector((state) => state.auth.userProfileFetched); // New flag
//     const navigate=useNavigate();
//     const [appLoading, setAppLoading] = useState(true); // New state for overall app loading

//  const logout = () => {
//     dispatch(logoutAction());
//     localStorage.removeItem("token");
//     localStorage.removeItem("userId");
//     navigate("/");
//   };

//     // Effect to dispatch initial Keycloak credentials
//    useEffect(() => {
//     if (token && tokenData && !userId) {
//         dispatch(setCredentials({ token, keycloakUser: tokenData }));
        
//         // ✅ Save token and userId to localStorage for API interceptor
//         localStorage.setItem('token', token);
//         localStorage.setItem('userId', tokenData.sub); // assuming sub is keycloak ID
//     }
// }, [token, tokenData, userId, dispatch]);


//     // Effect to fetch user profile from backend after Keycloak auth and initial dispatch
//     useEffect(() => {
//         const fetchAndSetUserProfile = async () => {
//             if (userId && !userProfileFetched) { // Only fetch if userId exists and profile not yet fetched
//                 try {
//                     const response = await getUserProfile(userId);
//                     dispatch(setUserProfile(response.data)); // Store full profile in Redux
//                 } catch (error) {
//                     console.error('Failed to fetch user profile:', error);
//                     // If the user does not exist in DB, this will likely be a 500 error from your UserService.
//                     // You might want to display a message or direct them to an "initial setup" page if they are expected to exist.
//                 } finally {
//                     setAppLoading(false); // Done loading initial user data
//                 }
//             } else if (!token) { // If no token, no need to fetch profile, just set appLoading to false
//                 setAppLoading(false);
//             } else if (userProfileFetched) { // If already fetched, no need to load
//                 setAppLoading(false);
//             }
//         };

//         if (token && tokenData && userId && !userProfileFetched) {
//              fetchAndSetUserProfile();
//         } else if (!token) { // If not logged in, no need for profile fetch
//             setAppLoading(false);
//         } else if (userProfileFetched) { // If already fetched, no need to load
//             setAppLoading(false);
//         }
//     }, [token, userId, userProfileFetched, dispatch]); // Depend on token, userId, and userProfileFetched

//     // --- Conditional Redirection Logic ---
//     const ConditionalRoute = ({ children }) => {
//         if (appLoading) {
//             return (
//                 <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
//                     <CircularProgress />
//                     <Typography sx={{ml:2}}>Loading user data...</Typography>
//                 </Box>
//             );
//         }

//         if (!token) { // Not authenticated, redirect to login page
//             return (
//                 <Box
//                     sx={{
//                         height: "100vh",
//                         display: "flex",
//                         flexDirection: "column",
//                         alignItems: "center",
//                         justifyContent: "center",
//                         textAlign: "center",
//                     }}
//                 >
//                     <Typography variant="h4" gutterBottom>
//                         Welcome to the Fitness Tracker App
//                     </Typography>
//                     <Typography variant="subtitle1" sx={{ mb: 3 }}>
//                         Please login to access your activities
//                     </Typography>
//                     <Button variant="contained" color="primary" size="large" onClick={() => logIn()}>
//                         LOGIN
//                     </Button>
//                 </Box>
//             );
//         }

//         // Authenticated. Now check profile completion
//         if (userProfile && !userProfile.profileCompleted) {
//             // User logged in but profile is not completed
//             // If current path is not '/profile-completion', navigate there
//             const location = useLocation(); // Hook to get current location
//             if (location.pathname !== '/profile-completion') {
//                 return <Navigate to="/profile-completion" replace />;
//             }
//         } else if (userProfile && userProfile.profileCompleted) {
//             // User logged in and profile is completed
//             // If current path is '/profile-completion' or '/', navigate to '/activities'
//             const location = useLocation();
//             if (location.pathname === '/' || location.pathname === '/profile-completion') {
//                 return <Navigate to="/activities" replace />;
//             }
//         }
//         // If profile status doesn't require navigation (e.g., already on the correct page), render children
//         return children;
//     };


//     return (
        
//             <ConditionalRoute>
//                 <Box sx={{ p: 2, border: '1px dashed grey' }}>
//                     <Button variant="contained" color="secondary" onClick={logout} sx={{ mb: 2 }}>
//                         Logout
//                     </Button>
//                     <Routes>
//                         <Route path="/activities" element={<ActvitiesPage />} />
//                         <Route path="/activities/:id" element={<ActivityDetail />} />
//                         {/* NEW Route for Profile Completion Form */}
//                         <Route path="/profile-completion" element={<ProfileCompletionForm />} />
//                         {/* Fallback route for authenticated users. ConditionalRoute handles redirection */}
//                         <Route path="/" element={<div style={{textAlign: 'center', marginTop: '50px'}}>Loading Fitness App...</div>} />
                        
//                     </Routes>
//                 </Box>
//             </ConditionalRoute>
        
//     );
// }
function App() {
    const { token, tokenData, logIn, isAuthenticated, logOut } = useContext(AuthContext);
    const dispatch = useDispatch();
    const userProfile = useSelector((state) => state.auth.user);
    const userId = useSelector((state) => state.auth.userId);
    const userProfileFetched = useSelector((state) => state.auth.userProfileFetched);
    const navigate = useNavigate();
    const location = useLocation(); // Moved out for reuse
    const [appLoading, setAppLoading] = useState(true);

    // ✅ FIX: Proper Keycloak logout
    const logout = () => {
        dispatch(logoutAction());
        localStorage.clear(); // Clear all
        logOut(); // <-- this calls Keycloak logout and uses logoutRedirectUri
    };

    useEffect(() => {
        if (token && tokenData && !userId) {
            dispatch(setCredentials({ token, keycloakUser: tokenData }));
            localStorage.setItem('token', token);
            localStorage.setItem('userId', tokenData.sub);
        }
    }, [token, tokenData, userId, dispatch]);

    useEffect(() => {
        const fetchAndSetUserProfile = async () => {
            try {
                if (token && userId && !userProfileFetched) {
                    const response = await getUserProfile(userId);
                    dispatch(setUserProfile(response.data));
                }
            } catch (error) {
                console.error("Error fetching user profile", error);
            } finally {
                setAppLoading(false); // ✅ Always stop loading
            }
        };

        // Delay until token is ready
        if (token && userId && !userProfileFetched) {
            fetchAndSetUserProfile();
        } else if (!token || userProfileFetched) {
            setAppLoading(false);
        }
    }, [token, userId, userProfileFetched, dispatch]);

    // ✅ FIX: Clear Welcome/Login separation
    const ConditionalRoute = ({ children }) => {
        if (appLoading) {
            return (
                <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                    <CircularProgress />
                    <Typography sx={{ ml: 2 }}>Loading user data...</Typography>
                </Box>
            );
        }

        // Not logged in → show Welcome Page
       if (!token) {
    return <WelcomePage/>;
}


        // Logged in but profile incomplete → Redirect
        if (userProfile && !userProfile.profileCompleted && location.pathname !== '/profile-completion') {
            return <Navigate to="/profile-completion" replace />;
        }

        // Logged in and profile completed → redirect to main
        if (userProfile?.profileCompleted && (location.pathname === '/' || location.pathname === '/profile-completion')) {
            return <Navigate to="/activities" replace />;
        }

        // Else render children
        return children;
    };

    return (
        <ConditionalRoute>
            <Box sx={{ p: 2, border: '1px dashed grey' }}>
                <Button variant="contained" color="secondary" onClick={logout} sx={{ mb: 2 }}>
                    Logout
                </Button>
                <Routes>
                    <Route path="/activities" element={<ActvitiesPage />} />
                    <Route path="/activities/:id" element={<ActivityDetail />} />
                    <Route path="/profile-completion" element={<ProfileCompletionForm />} />
                    <Route path="/" element={<div style={{ textAlign: 'center', marginTop: '50px' }}>Loading Fitness App...</div>} />
                </Routes>
            </Box>
        </ConditionalRoute>
    );
}

export default App;