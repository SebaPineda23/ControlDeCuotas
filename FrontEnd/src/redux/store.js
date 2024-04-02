import { configureStore } from "@reduxjs/toolkit";
import { combineReducers } from "redux";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage"; // defaults to localStorage for web

import sociosSlice from "./setSocios";
import userSlicec from "./setUsuario";

const persistConfig = {
  key: "root",
  storage,
};

const rootReducer = combineReducers({
  socios: sociosSlice,
  user: userSlicec,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
});

export const persistor = persistStore(store); // Asegúrate de exportar persistor aquí
