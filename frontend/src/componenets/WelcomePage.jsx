import React from "react";
import { AppBar, Toolbar, Typography, Button, Box, IconButton } from "@mui/material";
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter";
import { motion } from "framer-motion";
import { useContext } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { setCredentials } from "../store/authSlice";

const WelcomePage = () => {
  const { logIn } = useContext(AuthContext);

  return (
    <Box sx={{ height: "100vh", display: "flex", flexDirection: "column" }}>
      {/* Navbar */}
      <AppBar position="static" sx={{ bgcolor: "#ff4081" }}>
        <Toolbar>
          <IconButton edge="start" color="inherit" sx={{ mr: 1 }}>
            <FitnessCenterIcon />
          </IconButton>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            FitnessFreak
          </Typography>
          <Button color="inherit">Home</Button>
          <Button color="inherit">About Us</Button>
          {/* <Button color="inherit" onClick={() => logIn()}>
            Login
          </Button> */}
          <Button color="inherit" variant="outlined" sx={{ ml: 1 }} onClick={() => logIn()}>
           Login / SignUp
          </Button>
        </Toolbar>
      </AppBar>

      {/* Hero Section */}
      <Box
        sx={{
          flexGrow: 1,
          backgroundImage: "url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?auto=format&fit=crop&w=1950&q=80')",
          backgroundSize: "cover",
          backgroundPosition: "center",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          flexDirection: "column",
          textAlign: "center",
          color: "#fff",
        }}
      >
        <motion.div
          initial={{ opacity: 0, y: 40 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1 }}
        >
          <Typography variant="h3" sx={{ fontWeight: "bold", mb: 2, color: "pink" }}>
  Your AI Fitness Partner
</Typography>

<Typography variant="h6" sx={{ mb: 4, color: "pink" }}>
  Personalized workouts and tracking made smart
</Typography>
          <Button
            variant="contained"
            size="large"
            sx={{ bgcolor: "#ff4081", ":hover": { bgcolor: "#f50057" } }}
            onClick={() => logIn()}
          >
            Get Started
          </Button>
        </motion.div>
      </Box>
    </Box>
  );
};

export default WelcomePage;