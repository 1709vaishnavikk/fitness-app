import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router';
import { Box, TextField, Button, Typography, Paper, MenuItem, Grid } from '@mui/material';
import { updateUserProfile } from '../services/api';
// import { updateUserProfile, getUserProfile } from "../services/api"; // Import getUserProfile too
// import { setUserProfile } from '../store/authSlice'; 
import {setUserProfile} from '../store/authSlice'

const dietTypes = [
    { value: 'VEGETARIAN', label: 'Veg' },
    { value: 'NON_VEGETARIAN', label: 'Non-Veg' },
    { value: 'VEGAN', label: 'Vegan' },
    // Add other diet types if you have them in your backend enum
];

const ProfileCompletionForm = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userId = useSelector((state)=>state.auth.userId); // This is the Keycloak ID
    const currentUserProfile = useSelector((state) => state.auth.user); // The profile from backend if already fetched

    const [formData, setFormData] = useState({
        heightCm: '',
        weightKg: '',
        age: '',
        goalWeightKg: '',
        dailyWaterIntakeLiters: '',
        dietType: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Populate form if user profile already exists (e.g., editing)
    useEffect(() => {
        if (currentUserProfile) {
            setFormData({
                heightCm: currentUserProfile.heightCm || '',
                weightKg: currentUserProfile.weightKg || '',
                age: currentUserProfile.age || '',
                goalWeightKg: currentUserProfile.goalWeightKg || '',
                dailyWaterIntakeLiters: currentUserProfile.dailyWaterIntakeLiters || '',
                dietType: currentUserProfile.dietType || '',
            });
        }
    }, [currentUserProfile]);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        try {
            // Send update to your Gateway's /api/users/{keycloakId}/profile endpoint
            const updatedProfile = await updateUserProfile(userId, formData);
            dispatch(setUserProfile(updatedProfile.data)); // Update Redux state with new profile

            navigate('/activities'); // Redirect to activities page after completion
        } catch (err) {
            console.error('Failed to update profile:', err);
            setError('Failed to update profile. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 3 }}>
            <Paper elevation={3} sx={{ p: 4, maxWidth: 600, width: '100%' }}>
                <Typography variant="h5" component="h2" gutterBottom align="center">
                    Complete Your Profile
                </Typography>
                <Typography variant="body2" color="text.secondary" align="center" sx={{ mb: 3 }}>
                    Please provide some additional details to get personalized recommendations.
                </Typography>
                <form onSubmit={handleSubmit}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                label="Height (cm)"
                                name="heightCm"
                                type="number"
                                value={formData.heightCm}
                                onChange={handleChange}
                                fullWidth
                                required
                                inputProps={{ min: 50, max: 250, step: 0.1 }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                label="Weight (kg)"
                                name="weightKg"
                                type="number"
                                value={formData.weightKg}
                                onChange={handleChange}
                                fullWidth
                                required
                                inputProps={{ min: 20, max: 300, step: 0.1 }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                label="Age"
                                name="age"
                                type="number"
                                value={formData.age}
                                onChange={handleChange}
                                fullWidth
                                required
                                inputProps={{ min: 10, max: 100 }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                label="Goal Weight (kg)"
                                name="goalWeightKg"
                                type="number"
                                value={formData.goalWeightKg}
                                onChange={handleChange}
                                fullWidth
                                inputProps={{ min: 20, max: 300, step: 0.1 }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                label="Daily Water Intake (liters)"
                                name="dailyWaterIntakeLiters"
                                type="number"
                                value={formData.dailyWaterIntakeLiters}
                                onChange={handleChange}
                                fullWidth
                                inputProps={{ min: 0.5, max: 10, step: 0.1 }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                select
                                label="Diet Type"
                                name="dietType"
                                value={formData.dietType}
                                onChange={handleChange}
                                fullWidth
                                required
                            >
                                {dietTypes.map((option) => (
                                    <MenuItem key={option.value} value={option.value}>
                                        {option.label}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </Grid>
                    </Grid>
                    {error && (
                        <Typography color="error" sx={{ mt: 2, textAlign: 'center' }}>
                            {error}
                        </Typography>
                    )}
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        fullWidth
                        sx={{ mt: 3 }}
                        disabled={loading}
                    >
                        {loading ? 'Saving...' : 'Complete Profile'}
                    </Button>
                </form>
            </Paper>
        </Box>
    );
};

export default ProfileCompletionForm;