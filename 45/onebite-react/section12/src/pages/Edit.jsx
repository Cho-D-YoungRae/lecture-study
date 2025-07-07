import React from 'react';
import {useParams} from "react-router-dom";

function Edit() {
  const { id } = useParams();
  return (
    <div>{id} Edit</div>
  );
}

export default Edit;