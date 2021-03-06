INSERT INTO vacancies (
        id,
        name,
        url,
        description,
        schedule_id,
        accept_handicapped,
        experience_id,
        address_building,
        address_city,
        address_street,
        address_description,
        address_raw,
        address_lat,
        address_lng,
        employment_id,
        salary_from,
        salary_to,
        salary_currency,
        is_archived,
        area_id,
        createdAt,
        employer_url
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING;