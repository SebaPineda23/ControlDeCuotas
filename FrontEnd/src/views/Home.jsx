import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar'
import { Button } from 'antd';

export default function Home() {
    const navigate=useNavigate();
    const goForm =()=>navigate("/nuevoSocio")
  return (
    <div className='flex flex-col'>
        <div><SearchBar /></div>
        <div className='mt-5'><Button onClick={goForm} className = 'bg-blue-700' type="primary">Agregar socio</Button></div>
    </div>
  )
}
