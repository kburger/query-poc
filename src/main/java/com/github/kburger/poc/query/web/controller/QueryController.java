/*
 * MIT License
 *
 * Copyright (c) 2020 kburger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.kburger.poc.query.web.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.rdf4j.federated.FedXFactory;
import org.eclipse.rdf4j.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.kburger.poc.query.model.QueryRequest;

@RestController
@RequestMapping("${poc.query.endpoint:/}")
public class QueryController {
    @PostMapping
    public void executeQuery(@RequestBody QueryRequest request, HttpServletResponse response) throws IOException {
        final Repository repository;
        try {
            repository = FedXFactory.createSparqlFederation(request.getEndpoints());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        Repositories.tupleQuery(repository, request.getQuery(), new SPARQLResultsJSONWriter(response.getOutputStream()));
        
        repository.shutDown();
    }
}
