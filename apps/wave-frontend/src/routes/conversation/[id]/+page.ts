import { redirect } from "@sveltejs/kit";

export const load = ({ params }) => {
    if (params.id.length != 6 || isNaN(+params.id)) {
        throw redirect(301, "/");
    }

    return {
        chatId: +params.id,
    };
};
