import React from 'react';

function Button({children, text, color = "black"}) {
  return (
    <button style={{ color: color }}>
      {text} - {color}
      {children}
    </button>
  );
}

export default Button;