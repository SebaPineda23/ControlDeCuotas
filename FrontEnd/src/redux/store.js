import { configureStore } from '@reduxjs/toolkit'
import sociosSlice from "./setSocios"

export const store = configureStore({
    reducer: {
        socios: sociosSlice
    },
})