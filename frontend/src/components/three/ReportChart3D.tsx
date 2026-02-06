import { useRef, useMemo } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { OrbitControls, Text } from '@react-three/drei'
import * as THREE from 'three'

interface SegmentProps {
  startAngle: number
  endAngle: number
  color: string
  label: string
  value: number
  radius: number
}

function DonutSegment({ startAngle, endAngle, color, label, value, radius }: SegmentProps) {
  const meshRef = useRef<THREE.Mesh>(null)

  useFrame((state) => {
    if (meshRef.current) {
      meshRef.current.position.y = Math.sin(state.clock.elapsedTime * 0.8 + startAngle) * 0.05
    }
  })

  const geometry = useMemo(() => {
    const shape = new THREE.Shape()
    const innerRadius = radius * 0.5
    const outerRadius = radius

    shape.absarc(0, 0, outerRadius, startAngle, endAngle, false)
    shape.absarc(0, 0, innerRadius, endAngle, startAngle, true)
    shape.closePath()

    const extrudeSettings = { depth: 0.3, bevelEnabled: false }
    return new THREE.ExtrudeGeometry(shape, extrudeSettings)
  }, [startAngle, endAngle, radius])

  const midAngle = (startAngle + endAngle) / 2
  const labelRadius = radius * 1.3

  return (
    <group>
      <mesh ref={meshRef} geometry={geometry} rotation={[-Math.PI / 2, 0, 0]}>
        <meshStandardMaterial color={color} roughness={0.3} metalness={0.4} />
      </mesh>
      <Text
        position={[Math.cos(midAngle) * labelRadius, 0.3, -Math.sin(midAngle) * labelRadius]}
        fontSize={0.15}
        color="#042f2e"
        anchorX="center"
        anchorY="middle"
        font={undefined}
      >
        {`${label}: ${value}`}
      </Text>
    </group>
  )
}

interface ReportChart3DProps {
  overdue?: number
  active?: number
  returned?: number
}

export function ReportChart3D({ overdue = 3, active = 5, returned = 12 }: ReportChart3DProps) {
  const total = overdue + active + returned || 1

  const segments = useMemo(() => {
    let currentAngle = 0
    const items = [
      { label: 'Overdue', value: overdue, color: '#dc2626' },
      { label: 'Active', value: active, color: '#0d9488' },
      { label: 'Returned', value: returned, color: '#16a34a' },
    ]

    return items.map((item) => {
      const angle = (item.value / total) * Math.PI * 2
      const segment = {
        ...item,
        startAngle: currentAngle,
        endAngle: currentAngle + angle,
        radius: 1.2,
      }
      currentAngle += angle
      return segment
    })
  }, [overdue, active, returned, total])

  return (
    <div className="w-full h-64 rounded-xl overflow-hidden bg-gradient-to-b from-teal-50 to-white border border-teal-100">
      <Canvas camera={{ position: [0, 3, 3], fov: 40 }}>
        <ambientLight intensity={0.6} />
        <directionalLight position={[5, 8, 5]} intensity={0.7} />
        <pointLight position={[-2, 2, 2]} intensity={0.3} color="#0d9488" />

        {segments.map((seg) => (
          <DonutSegment key={seg.label} {...seg} />
        ))}

        <OrbitControls
          enablePan={false}
          enableZoom={false}
          autoRotate
          autoRotateSpeed={1}
          minPolarAngle={Math.PI / 6}
          maxPolarAngle={Math.PI / 2.5}
        />
      </Canvas>
    </div>
  )
}
