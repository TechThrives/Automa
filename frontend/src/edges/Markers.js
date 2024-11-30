import React from 'react'

const Markers = () => {
  return (
    <div><svg>
    <defs>
      <marker
        id="arrowclosed"
        markerWidth="12.5"
        markerHeight="12.5"
        viewBox="-10 -10 20 20"
        markerUnits="strokeWidth"
        orient="auto-start-reverse"
        refX="0"
        refY="0"
      >
        <polyline
          strokeLinecap="round"
          strokeLinejoin="round"
          points="-5,-4 0,0 -5,4 -5,-4"
          style={{
            stroke: "rgb(178, 178, 178)",
            fill: "rgb(178, 178, 178)",
            strokeWidth: 1,
          }}
        ></polyline>
      </marker>

      <marker
        id="arrowclosedgray"
        markerWidth="12.5"
        markerHeight="12.5"
        viewBox="-10 -10 20 20"
        markerUnits="strokeWidth"
        orient="auto-start-reverse"
        refX="0"
        refY="0"
      >
        <polyline
          strokeLinecap="round"
          strokeLinejoin="round"
          points="-5,-4 0,0 -5,4 -5,-4"
          style={{
            stroke: "rgb(85, 85, 85)",
            fill: " rgb(85, 85, 85)",
            strokeWidth: 1,
          }}
        ></polyline>
      </marker>
    </defs>
  </svg></div>
  )
}

export default Markers