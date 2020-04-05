import React from 'react';
const HorizontalLine = ({ color="black", bHeight=2, bStyle="solid" }) => (
    <hr
        style={{
            borderTopColor: color,
            borderTopWidth: bHeight,
            borderStyle: bStyle
        }}
    />
);
export default HorizontalLine;