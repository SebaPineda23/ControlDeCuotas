import { createSlice } from "@reduxjs/toolkit";
import { act } from "react";

const initialState = {
  allSocios: [],
  sociosFiltered: [],
  editSocio: [],
  historial: [],
  allPagos: [],
  montoTotal: 0,
  mes: "",
  año: "",
  categoria: "",
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
    setMontoTotal: (state, action) => {
      state.montoTotal = action.payload;
    },
    setMes: (state, action) => {
      state.mes = action.payload;
    },
    setAño: (state, action) => {
      state.año = action.payload;
    },
    setCategoria: (state, action) => {
      state.categoria = action.payload;
    },
  },
});

export const getAllSocios = (state) => state.socios.allSocios;
export const getSociosFiltered = (state) => state.socios.sociosFiltered;
export const getFilterSocios = (state) => state.socios.editSocio;
export const getHistorial = (state) => state.socios.historial;
export const getAllPagos = (state) => state.socios.allPagos;
export const getMontoTotal = (state) => state.socios.montoTotal;
export const getMes = (state) => state.socios.mes;
export const getAño = (state) => state.socios.año;
export const getCategoria = (state) => state.socios.categoria;

export const {
  setAllSocios,
  setFilterSocios,
  setEditSocio,
  setHistorial,
  setAllPagos,
  setStates,
  setMontoTotal,
  setMes,
  setAño,
  setCategoria,
} = sociosSlice.actions;

export default sociosSlice.reducer