import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  username: "",
  password: "",
  access: false,
  user: [],
};

const userSlicec = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload;
    },
    setAccess: (state, action) => {
      state.access = action.payload;
    },
    setUsername: (state, action) => {
      state.username = action.payload;
    },
    setPassword: (state, action) => {
      state.password = action.payload;
    },
    logout: (state) => {
      state.access = false;
    },
    login: (state) => {
      state.access = true;
    },
  },
});
export const getAccess = (state) => state.user.access;
export const { setUsername, setPassword, setAccess, setUser, logout, login } =
  userSlicec.actions;

export default userSlicec.reducer;
