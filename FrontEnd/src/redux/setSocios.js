import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  allSocios: [],
  sociosFiltered: [],
  editSocio: [],
};

const sociosSlice = createSlice({
  name: "socios",
  initialState,
  reducers: {
    setAllSocios: (state, action) => {
      state.allSocios = action.payload;
    },
    setFilterSocios: (state, action) => {
      state.sociosFiltered = action.payload;
    },
    setEditSocio: (state, action) => {
      state.editSocio = action.payload;
    },
  },
});

export const getAllSocios = (state) => state.socios.allSocios;
export const getSociosFiltered = (state) => state.socios.sociosFiltered;
export const getFilterSocios = (state) => state.socios.editSocio;

export const { setAllSocios, setFilterSocios, setEditSocio } =
  sociosSlice.actions;

export default sociosSlice.reducer