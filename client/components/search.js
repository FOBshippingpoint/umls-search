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

  const searchUMLS = async (searchText) => {
    let response;

    // Check if searchText is a valid CUI (only alphanumeric characters and length of 8)
    const isCUI = /^[a-zA-Z0-9]{8}$/.test(searchText);

    if (isCUI) {
      response = await axios.get(
        `${process.env.NEXT_PUBLIC_SSCS_UMLS_BASE_URL}/umls/search/cui/${searchText}`
      );
    } else {
      response = await axios.get(
        `${process.env.NEXT_PUBLIC_SSCS_UMLS_BASE_URL}/umls/search/text/${searchText}`
      );
    }

    return response;
  };

  const search = async (text) => {
    setLoading(true);
    setSearchResults([]);
    setTotalResults(0);
    try {
      const response = await searchUMLS(text);
      setSearchResults(response.data?.results);
      setTotalResults(response.data?.totalCount);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const debouncedSearch = useDebouncedCallback(search, 200);

  return (
    <div className="container mx-auto p-4">
      <TextField
        label="Search for CUI or text"
        value={searchText}
        onChange={handleSearchTextChange}
        fullWidth
      />
      {loading && <CircularProgress />}
      {!loading && (
        <div className="mt-4">
          <Typography variant="h6" className="text-lg font-semibold">
            Total results: {totalResults}
          </Typography>
          <ul className="list-none">
            {searchResults.map((result) => (
              <li
                key={result.id}
                className="border-b border-gray-200 py-2 text-base"
              >
                <Typography>
                  <span className="font-semibold">{result.cui}</span>:{" "}
                  {result.definition}
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
