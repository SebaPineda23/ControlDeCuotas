import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  allSocios: [],
  sociosFiltered: [],
  editSocio: [],
  historial: [],
  allPagos: [],
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
    setHistorial: (state, action) => {
      state.historial = action.payload;
    },
    setAllPagos: (state, action) => {
      state.allPagos = action.payload;
    },
  },
});

export const getAllSocios = (state) => state.socios.allSocios;
export const getSociosFiltered = (state) => state.socios.sociosFiltered;
export const getFilterSocios = (state) => state.socios.editSocio;
export const getHistorial = (state) => state.socios.historial;
export const getAllPagos = (state) => state.socios.allPagos;
export const {
  setAllSocios,
  setFilterSocios,
  setEditSocio,
  setHistorial,
  setAllPagos,
  setStates,
} = sociosSlice.actions;

export default sociosSlice.reducer