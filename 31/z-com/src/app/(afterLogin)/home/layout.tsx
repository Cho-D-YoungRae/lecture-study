import React from "react";

export default async function HomeLayout(
    {children}: Readonly<{children: React.ReactNode}>
) {

    return (
        <div>
            Home Layout
            {children}
        </div>
    )
}