import React from 'react';
import { useSelector } from 'react-redux';
import { Box, Typography, Paper } from '@mui/material';

const UserProfileDisplay = () => {
    const userProfile = useSelector((state) => state.auth.user); // The full UserResponse from backend

    if (!userProfile) {
        return <Typography variant="body2" color="text.secondary">Loading profile...</Typography>;
    }

    return (
        <Paper elevation={1} sx={{ p: 2, mb: 3, bgcolor: 'background.paper', borderRadius: 2 }}>
            <Typography variant="h6" gutterBottom>
                Your Profile Summary
            </Typography>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 1 }}>
                <Typography variant="body2"><strong>Name:</strong> {userProfile.firstName} {userProfile.lastName}</Typography>
                <Typography variant="body2"><strong>Email:</strong> {userProfile.email}</Typography>
                <Typography variant="body2"><strong>Age:</strong> {userProfile.age || 'N/A'} years</Typography>
                <Typography variant="body2"><strong>Height:</strong> {userProfile.heightCm ? `${userProfile.heightCm} cm` : 'N/A'}</Typography>
                <Typography variant="body2"><strong>Weight:</strong> {userProfile.weightKg ? `${userProfile.weightKg} kg` : 'N/A'}</Typography>
                <Typography variant="body2"><strong>Goal Weight:</strong> {userProfile.goalWeightKg ? `${userProfile.goalWeightKg} kg` : 'N/A'}</Typography>
                <Typography variant="body2"><strong>Water Intake:</strong> {userProfile.dailyWaterIntakeLiters ? `${userProfile.dailyWaterIntakeLiters} L` : 'N/A'}</Typography>
                <Typography variant="body2"><strong>Diet Type:</strong> {userProfile.dietType || 'N/A'}</Typography>
                {/* You can add a link to edit profile here if needed */}
            </Box>
        </Paper>
    );
};

export default UserProfileDisplay;