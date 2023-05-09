"use client";

import React, { useState, useCallback } from "react";
import { TextField, Typography, CircularProgress } from "@mui/material";
import axios from "axios";
import { useDebouncedCallback } from "use-debounce";

const Search = () => {
  const [searchText, setSearchText] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalResults, setTotalResults] = useState(0);

  const handleSearchTextChange = (event) => {
    setSearchText(event.target.value);
    debouncedSearch(event.target.value);
  };

  const search = async (text) => {
    setLoading(true);
    setSearchResults([]);
    setTotalResults(0);
    try {
      //   const response = await axios.get(
      //     `${process.env.NEXT_PUBLIC_SSCS_UMLS_BASE_URL}/umls/search/cui/${text}`
      //   );

      //   if (response?.totalCount === 0) {
      const response = await axios.get(
        `${process.env.NEXT_PUBLIC_SSCS_UMLS_BASE_URL}/umls/search/text/${text}`
      );
      //   }

      setSearchResults(response.data?.results);
      setTotalResults(response.data?.length);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const debouncedSearch = useDebouncedCallback(search, 200);

  return (
    <div>
      <TextField
        label="Search for CUI or text"
        value={searchText}
        onChange={handleSearchTextChange}
        fullWidth
      />
      {loading && <CircularProgress />}
      {!loading && (
        <div>
          <Typography variant="h6">Total results: {totalResults}</Typography>
          <ul>
            {searchResults.map((result) => (
              <li key={result.id}>
                <Typography>
                  {result.cui}: {result.definition}
                </Typography>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default Search;
