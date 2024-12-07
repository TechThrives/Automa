import React, { useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import { useAppContext } from '../../context/AppContext';

const Logout = () => {
    const navigate = useNavigate();
    const { setUser } = useAppContext();
    useEffect(() => {
        localStorage.removeItem("jwtToken");
        setUser(null);
        navigate("/");
    })
  return (
    <div>Logout</div>
  )
}

export default Logout