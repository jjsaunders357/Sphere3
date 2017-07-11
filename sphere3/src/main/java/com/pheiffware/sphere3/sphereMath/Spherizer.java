package com.pheiffware.sphere3.sphereMath;

/**
 * Created by Steve on 7/8/2017.
 */

public class Spherizer
{
    //Used in transformations
    private final Angle angle = Angle.newDegrees(0);

    //the ratio (degrees / EuclideanDistance) used in the transformation
    private final float degreesPerDistance;

    public Spherizer(float degreesPerDistance)
    {
        this.degreesPerDistance = degreesPerDistance;
    }

//    public Matrix4 spherizeMatrix(Matrix4 matrix)
//    {
////        Matrix4 sphereMatrix = new Matrix4();
//
//        //Scale is not allowed in final matrices.  It can be applied to the model's vertices directly and removed though.
//
//        //"Rotation":
//        //extract original rotation matrix
//
//        //"Translation": use magnitude of translation to convert to rotation
//        //sin 0 0 0
//        //0 sin 0 0
//        //0 0 sin 0
//        //0 0 0 cos
//    }

    /**
     * Transforms the given Euclidean space positions and normals into sphere space.  Each incoming position and normal is assumed to be homogeneous with 4-elements (w is ignored).
     * The array is overwritten with 4 dimensional coordinates representing points on the sphere's face and normals in 4D.
     *
     * @param positions homogeneous position vectors
     * @param normals   homogeneous normal vectors
     */
    public void spherizeVertices(float[] positions, float[] normals)
    {
        Vec4F position = new Vec4F(positions, 0);
        Vec4F normal = new Vec4F(normals, 0);
        do
        {
            spherizeVertex(position, normal);
        } while (position.next());
    }

    /**
     * Transforms a point/normal into sphere space.  Values are overwritten in incoming arrays
     *
     * @param position homogeneous position vector
     * @param normal   homogeneous normal vector
     */
    private void spherizeVertex(Vec4F position, Vec4F normal)
    {
        //W component is not used.
        position.setW(0);
        float magnitude = position.magnitude();
        angle.setDegrees(degreesPerDistance * magnitude);

        //Make position a unit vector
        position.scale(1f / magnitude);

        //Rotate [0,0,0,-1] towards position.  The amount to rotate is determined by the magnitude of the original vector.
        position.setX(position.x() * angle.sin);
        position.setY(position.y() * angle.sin);
        position.setZ(position.z() * angle.sin);
        position.setW(-1f * angle.cos);

        //Normal is rotated the same amount towards [0,0,0,1]
        normal.setX(normal.x() * angle.cos);
        normal.setY(normal.y() * angle.cos);
        normal.setZ(normal.z() * angle.cos);
        normal.setW(1f * angle.sin);
    }
}
