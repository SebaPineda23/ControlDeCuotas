import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    allSocios: [],
    sociosFiltered: [],
}

const sociosSlice = createSlice({
    name: "socios",
    initialState,
    reducers: {
        setAllSocios: (state, action) => {
            state.allSocios = action.payload
        },
        setFilterSocios: (state, action) => {
            state.sociosFiltered = action.payload
        }
    }
})

export const getAllSocios = (state) => state.socios.allSocios;
export const getSociosFiltered = (state) => state.socios.sociosFiltered

export const { setAllSocios, setFilterSocios } = sociosSlice.actions

export default sociosSlice.reducer